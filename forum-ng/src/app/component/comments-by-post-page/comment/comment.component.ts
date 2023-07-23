import {
   Component,
   ElementRef,
   EventEmitter,
   Input,
   OnInit,
   Output,
   ViewChild,
} from '@angular/core';
import { CommentModel } from 'src/app/model/comment-model';
import { EditCommentEvent } from 'src/app/event/edit-comment-event';
import { AuthService } from 'src/app/service/auth.service';
import { SubSink } from 'subsink';

@Component({
   selector: 'app-comment',
   templateUrl: './comment.component.html',
   styleUrls: ['./comment.component.scss'],
})
export class CommentComponent implements OnInit {
   private subs = new SubSink();

   @Input() c: CommentModel;
   @Input() replyingTo: string;

   // used for tracking consecutive replies to child comments
   // if > 10 don't use indent to indicate it is a reply
   @Input() replyDepth: number;

   @Output() deleteCommentEvent = new EventEmitter<number>();
   @Output() editCommentEvent = new EventEmitter<EditCommentEvent>();
   @Output() replyEvent = new EventEmitter<CommentModel>();
   @Output() likeEvent = new EventEmitter<number>();
   @Output() dislikeEvent = new EventEmitter<number>();

   @ViewChild('editCommentInput') editCommentInput: ElementRef;

   constructor(
      private authService: AuthService,
   ) {}

   ngOnInit(): void {

   }

   emitLikeEvent(commentId: number) {
         this.likeEvent.emit(commentId);
   }

   emitDislikeEvent(commentId: number) {
         this.dislikeEvent.emit(commentId);
   }

   showEditCommentInput() {
      console.log(this.editCommentInput);
      if (this.editCommentInput.nativeElement.classList.contains('d-inline-block')) {
         this.editCommentInput.nativeElement.classList.remove('d-inline-block');
      } else {
         this.editCommentInput.nativeElement.classList.add('d-inline-block');
      }
   }

   checkIfOwner(usernameToCheck: string): boolean {
      return this.authService.checkIfEntityIsOwned(usernameToCheck);
   }

   emitEditCommentEvent(comment: CommentModel, commentBody: string) {
      console.log('emitting edit event: ', new EditCommentEvent(comment, commentBody))
      this.editCommentEvent.emit(new EditCommentEvent(comment, commentBody));
   }

   handleChildCommentEditEvent(childCommentEvent: EditCommentEvent) {
      this.editCommentEvent.emit(childCommentEvent);
   }

   emitDeleteCommentEvent(commentId: number) {
      this.deleteCommentEvent.emit(commentId);
   }

   emitReplyEvent(comment: CommentModel) {
      this.replyEvent.emit(comment);
   }
}
