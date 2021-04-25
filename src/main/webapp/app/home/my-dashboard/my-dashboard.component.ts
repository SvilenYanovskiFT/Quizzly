import { Component, OnInit } from '@angular/core';
import { QuizService } from 'app/entities/quiz/service/quiz.service';

@Component({
  selector: 'jhi-my-dashboard',
  templateUrl: './my-dashboard.component.html',
  styleUrls: ['./my-dashboard.component.scss'],
})
export class MyDashboardComponent implements OnInit {
  constructor(protected quizService: QuizService) {}

  ngOnInit(): void {
    console.log();
  }
}
