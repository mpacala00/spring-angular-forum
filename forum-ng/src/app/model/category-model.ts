import { PostModel } from "./post-model";

export interface CategoryModel {
   id: number;
   name: string;
   description: string;
   posts: PostModel[];
   userFollowing: boolean;
}