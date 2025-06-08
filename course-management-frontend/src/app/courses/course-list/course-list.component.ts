import {Component, inject, OnInit} from '@angular/core';
import { Course } from '../../models/course.model';
import { CourseService } from '../../services/course.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-course-list',
  templateUrl: './course-list.component.html',
  styleUrls: ['course-list.component.css'],
})
export class CourseListComponent implements OnInit {
  courses: Course[] = [];
  loading = false;
  error = '';
  pageNumber = 0;
  pageSize = 10;
  totalPages = 0;
  totalElements = 0;
  statusFilter = 'ALL';
  averageDuration?: number;
  toastMessage = '';
  toastType: 'success' | 'error' = 'success';

  showToast(message: string, type: 'success' | 'error' = 'success') {
    this.toastMessage = message;
    this.toastType = type;
    setTimeout(() => (this.toastMessage = ''), 3000);
  }

  courseService = inject(CourseService);
  router = inject(Router);

  ngOnInit(): void {
    this.loadCourses();
  }

  loadCourses() {
    this.loading = true;
    this.courseService.getAll(this.pageNumber, this.pageSize, this.statusFilter).subscribe({
      next: (data) => {
        this.courses = data.content;
        this.pageNumber = data.number;
        this.pageSize = data.size;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load courses';
        this.loading = false;
      },
    });
  }

  deleteCourse(id: number) {
    if (confirm('Are you sure you want to delete this course?')) {
      this.courseService.delete(id).subscribe({
        next: () => this.loadCourses(),
        error: () => alert('Failed to delete course'),
      });
    }
  }

  publishCourse(id: number) {
    this.courseService.publish(id).subscribe({
      next: () => this.loadCourses(),
      error: () => alert('Failed to publish course'),
    });
  }

  archiveCourse(id: number) {
    this.courseService.archive(id).subscribe({
      next: () => this.loadCourses(),
      error: () => alert('Failed to archive course'),
    });
  }

  changePage(newPage: number) {
    if (newPage >= 0 && newPage < this.totalPages) {
      this.pageNumber = newPage;
      this.loadCourses();
    }
  }

  onFilterChange() {
    this.pageNumber = 0;
    this.loadCourses();
  }

  hasPublished(): boolean {
    return this.courses.some((course) => course.status === 'PUBLISHED');
  }

  getAverageDuration() {
    this.courseService.getAverageDuration().subscribe({
      next: (avg) => {
        this.averageDuration = avg;
        this.showToast(`Average duration: ${avg.toFixed(2)} min`, 'success');
      },
      error: () => {
        this.showToast('Failed to fetch average duration', 'error');
      },
    });
  }

  duplicateCourse(id: number) {
    this.courseService.duplicate(id).subscribe({
      next: () => {
        this.showToast('Course duplicated successfully', 'success');
        this.loadCourses();
      },
      error: () => {
        this.showToast('Failed to duplicate course', 'error');
      },
    });
  }
}
