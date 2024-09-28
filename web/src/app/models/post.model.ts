export class Post {
  id: string;
  content: string;
  version: number;

  constructor(id: string, content: string, version: number) {
    this.id = id;
    this.content = content;
    this.version = version
  }
}
