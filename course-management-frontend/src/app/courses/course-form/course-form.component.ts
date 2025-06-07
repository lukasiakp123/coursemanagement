import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CourseService } from '../../services/course.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Course } from '../../models/course.model';

@Component({
  selector: 'app-course-form',
  templateUrl: './course-form.component.html',
  styleUrls: []
})
export class CourseFormComponent implements OnInit {

  courseForm!: FormGroup;
  isEditMode = false;
  courseId!: number;
  loading = false;
  error = '';

  constructor(
    private fb: FormBuilder,
    private courseService: CourseService,
    private route: ActivatedRoute,
    protected router: Router
  ) { }

  ngOnInit(): void {
    this.courseForm = this.fb.group({
      title: ['', Validators.required],
      description: [''],
      duration: [1, [Validators.required, Validators.min(1)]]
    });

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.courseId = +id;
        this.loadCourse(this.courseId);
      }
    });
  }

  loadCourse(id: number) {
    this.loading = true;
    this.courseService.getById(id).subscribe({
      next: (course) => {
        this.courseForm.patchValue({
          title: course.title,
          description: course.description,
          duration: course.duration
        });
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load course';
        this.loading = false;
      }
    });
  }

  onSubmit() {
    if (this.courseForm.invalid) {
      return;
    }
    this.loading = true;
    const courseData = this.courseForm.value;
    if (this.isEditMode) {
      this.courseService.update(this.courseId, courseData).subscribe({
        next: () => this.router.navigate(['/courses']),
        error: () => {
          this.error = 'Failed to update course';
          this.loading = false;
        }
      });
    } else {
      this.courseService.create(courseData).subscribe({
        next: () => this.router.navigate(['/courses']),
        error: () => {
          this.error = 'Failed to create course';
          this.loading = false;
        }
      });
    }
  }
}
