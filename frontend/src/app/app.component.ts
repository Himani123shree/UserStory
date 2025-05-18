import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';

  userStory = '';
  generatedSql = '';
  isLoading = false;

  constructor(private http: HttpClient) {}

  async submitUserStory() {
    this.isLoading = true;
    this.generatedSql = '';
    try {
      const response: any = await this.http.post(
        'http://localhost:8081/api/generate-sql',
        { userStory: this.userStory },
        { headers: { 'Content-Type': 'application/json' } }
      ).toPromise();
      this.generatedSql = response.sqlScript || 'No SQL script returned.';
    } catch (error: any) {
      if (error && error.error && error.error.sqlScript) {
        this.generatedSql = error.error.sqlScript;
      } else if (error && error.message) {
        this.generatedSql = 'Error: ' + error.message;
      } else {
        this.generatedSql = 'Error generating SQL script.';
      }
    } finally {
      this.isLoading = false;
    }
  }
}
