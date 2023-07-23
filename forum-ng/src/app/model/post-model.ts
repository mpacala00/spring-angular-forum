export interface PostModel {
   id: number;
   title: string;
   body: string;
   creator: string;
   postDate: string;
   comments: any[];
   likeCount: number;
   isLikedByUser: boolean;
}
