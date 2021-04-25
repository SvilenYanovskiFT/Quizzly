import { Component, OnInit } from '@angular/core';
import { QuizService } from 'app/entities/quiz/service/quiz.service';

@Component({
  selector: 'jhi-my-quizzes',
  templateUrl: './my-quizzes.component.html',
  styleUrls: ['./my-quizzes.component.scss'],
})
export class MyQuizzesComponent implements OnInit {
  constructor(protected quizService: QuizService) {}

  ngOnInit(): void {
    console.log();
  }
}
