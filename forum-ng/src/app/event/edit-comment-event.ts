import { CommentModel } from "../model/comment-model";

export class EditCommentEvent {
   comment: CommentModel;
   body: string;

   constructor(comment: CommentModel, body: string) {
      this.comment = comment;
      this.body = body;
   }
}
