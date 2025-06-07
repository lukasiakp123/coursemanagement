export interface Course {
  id: number;
  title: string;
  description?: string;
  duration: number;
  status: 'DRAFT' | 'PUBLISHED' | 'ARCHIVED';
  createdAt: string;
  publishedAt?: string;
}
