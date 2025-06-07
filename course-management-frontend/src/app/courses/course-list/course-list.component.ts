import { Component, OnInit } from '@angular/core';
import { Course } from '../../models/course.model';
import { CourseService } from '../../services/course.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-course-list',
  templateUrl: './course-list.component.html',
  styleUrls: []
})
export class CourseListComponent implements OnInit {

  courses: Course[] = [];
  loading = false;
  error = '';

  constructor(private courseService: CourseService, protected router: Router) { }

  ngOnInit(): void {
    this.loadCourses();
  }

  loadCourses() {
    this.loading = true;
    this.courseService.getAll().subscribe({
      next: data => {
        this.courses = data;
        this.loading = false;
      },
      error: err => {
        this.error = 'Failed to load courses';
        this.loading = false;
      }
    });
  }

  deleteCourse(id: number) {
    if (confirm('Are you sure you want to delete this course?')) {
      this.courseService.delete(id).subscribe({
        next: () => this.loadCourses(),
        error: () => alert('Failed to delete course')
      });
    }
  }

  publishCourse(id: number) {
    this.courseService.publish(id).subscribe({
      next: () => this.loadCourses(),
      error: () => alert('Failed to publish course')
    });
  }

  archiveCourse(id: number) {
    this.courseService.archive(id).subscribe({
      next: () => this.loadCourses(),
      error: () => alert('Failed to archive course')
    });
  }
}
