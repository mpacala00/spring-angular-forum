import { Post } from "./post";

export interface CategoryModel {
   id: number;
   name: string;
   description: string;
   posts: Post[];
}