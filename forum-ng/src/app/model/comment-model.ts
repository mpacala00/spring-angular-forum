export interface CommentModel {
   id: number;
   creator: string;
   postDate: string;
   body: string;
   childComments: CommentModel[];
   deleted: boolean;
   likeCount: number;
   isLikedByUser: boolean;
}