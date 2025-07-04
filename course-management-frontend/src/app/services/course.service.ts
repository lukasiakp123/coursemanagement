import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Course, CoursePage } from '../models/course.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CourseService {
  private apiUrl = `${environment.apiUrl}/courses`;

  http = inject(HttpClient);


  getAll(page = 0, size = 10, status?: string): Observable<CoursePage> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (status && status !== 'ALL') {
      params = params.set('status', status);
    }

    return this.http.get<CoursePage>(this.apiUrl, { params });
  }


  getById(id: number): Observable<Course> {
    return this.http.get<Course>(`${this.apiUrl}/${id}`);
  }

  create(course: Partial<Course>): Observable<Course> {
    return this.http.post<Course>(this.apiUrl, course);
  }

  update(id: number, course: Partial<Course>): Observable<Course> {
    return this.http.put<Course>(`${this.apiUrl}/${id}`, course);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  publish(id: number): Observable<Course> {
    return this.http.post<Course>(`${this.apiUrl}/${id}/publish`, {});
  }

  archive(id: number): Observable<Course> {
    return this.http.post<Course>(`${this.apiUrl}/${id}/archive`, {});
  }

  getAverageDuration() {
    return this.http.get<number>(`${this.apiUrl}/average-duration`);
  }

  duplicate(id: number) {
    return this.http.post<Course>(`${this.apiUrl}/${id}/duplicate`, {});
  }
}
