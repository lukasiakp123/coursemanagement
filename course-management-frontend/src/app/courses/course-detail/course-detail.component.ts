import {Component, inject, OnInit} from '@angular/core';
import { CourseService } from '../../services/course.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Course } from '../../models/course.model';

@Component({
  selector: 'app-course-detail',
  templateUrl: './course-detail.component.html',
  styleUrls: [],
})
export class CourseDetailComponent implements OnInit {
  course!: Course | null;
  loading = false;
  error = '';

  courseService = inject(CourseService);
  route = inject(ActivatedRoute);
  router = inject(Router);


  ngOnInit(): void {
    this.loadCourse();
  }

  loadCourse() {
    this.loading = true;
    const id = +this.route.snapshot.paramMap.get('id')!;
    this.courseService.getById(id).subscribe({
      next: (course) => {
        this.course = course;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load course';
        this.loading = false;
      },
    });
  }

  backToList() {
    this.router.navigate(['/courses']);
  }
}
