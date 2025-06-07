export enum CourseStatus {
  DRAFT = 'DRAFT',
  PUBLISHED = 'PUBLISHED',
  ARCHIVED = 'ARCHIVED'
}

export interface Course {
  id: number;
  title: string;
  description: string;
  duration: number;
  status: CourseStatus;
  createdAt: string;
  publishedAt?: string;
}

export interface CoursePage {
  content: Course[];
  number: number;
  size: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}
