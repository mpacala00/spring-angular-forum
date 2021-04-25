package com.github.mpacala00.forum.security.token;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.github.mpacala00.forum.service.DateService;

import java.util.Date;
import java.util.Map;

import static io.jsonwebtoken.Jwts.parser;
import static io.jsonwebtoken.impl.TextCodec.BASE64;
import static java.util.Objects.*;

//https://www.jsonwebtoken.io - simple implementation (encoding and decoding)

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtService implements TokenService, Clock //Clock is an interface provided by jwt.io
{
    public static final String DOT = ".";

    //jwt.io implementation of gzip compression
    public static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

    DateService dateService;
    String issuer;
    int expirationSec;
    int clockSkewSec;
    String secret;

    public JwtService(final DateService dateService,
                      @Value("${jwt.issuer}") final String issuer,
                      @Value("${jwt.expiration-sec}") int expirationSec,
                      @Value("${jwt.clock-skew-sec}") int clockSkewSec,
                      @Value("${jwt.secret}") String secret) {
        super();
        this.dateService = requireNonNull(dateService);
        this.issuer = requireNonNull(issuer);
        this.expirationSec = requireNonNull(expirationSec);

        //this field is for dealing with time variation between 2 parties
        //not necessary for small projects, but could be important if
        //2 or more authorization servers were used
        this.clockSkewSec = requireNonNull(clockSkewSec);
        this.secret = BASE64.encode(requireNonNull(secret));

        System.out.println(secret); //for debugging
    }

    /**
     *
     * @param attributes - claims to be added to the token
     * @param expiresInSeconds - if > 0 then add to current time and set expiration date on the token
     * @return
     */
    private String newToken(final Map<String, Object> attributes, final int expiresInSeconds) {
        final DateTime now = dateService.now();
        final Claims claims = Jwts.claims().setIssuer(issuer).setIssuedAt(now.toDate());

        if(expiresInSeconds > 0) {
            final DateTime expiresAt = now.plusSeconds(expiresInSeconds);
            claims.setExpiration(expiresAt.toDate());
        }
        claims.putAll(attributes);

        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secret)
//                .compressWith(COMPRESSION_CODEC) //somehow breaks the jwt
                .compact();
    }

    /**
     * @param attributes - claims for JWT
     * @return never-expiring JWT
     */
    @Override
    public String permanent(final Map<String, Object> attributes) {
        return newToken(attributes, 0);
    }

    /**
     *
     * @param attributes - claims for JWT
     * @return JWT expiring after certain amount of seconds
     * expirationSec is specified in application.yml
     */
    @Override
    public String expiring(Map<String, Object> attributes) {
        return newToken(attributes, expirationSec);
    }

    @Override
    public Map<String, String> verify(String token) {
        final JwtParser parser = Jwts
                .parser()
                .requireIssuer(issuer)
                .setClock(this)
                .setAllowedClockSkewSeconds(clockSkewSec)
                .setSigningKey(secret);
        return parseClaims(() -> parser.parseClaimsJws(token).getBody());
    }

    @Override
    public Map<String, String> untrusted(String token) {
        final JwtParser parser = parser()
                .requireIssuer(issuer)
                .setClock(this)
                .setAllowedClockSkewSeconds(clockSkewSec);

        //https://github.com/jwtk/jjwt/issues/135
        //reading the payload but not giving authorization
        final String withoutSignature = StringUtils.substringBeforeLast(token, DOT) + DOT;
        return parseClaims(() -> parser.parseClaimsJws(withoutSignature).getBody());
    }

    private static Map<String, String> parseClaims(final Supplier<Claims> toClaims) {
        try {
            final Claims claims = toClaims.get();
            final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
            //for each map entry of the specified type in claims Map
            for(final Map.Entry<String, Object> e: claims.entrySet()) {
                builder.put(e.getKey(), String.valueOf(e.getValue()));
            }
            return builder.build();
        } catch (final IllegalArgumentException | JwtException e) {
            //return empty map from google guava lib
            return ImmutableMap.of();
        }
    }

    @Override
    public Date now() {
        final DateTime now = dateService.now();
        return now.toDate();
    }
}

