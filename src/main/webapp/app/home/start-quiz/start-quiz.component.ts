import { Component, OnInit } from '@angular/core';
import { QuizService } from 'app/entities/quiz/service/quiz.service';
import { ActivatedRoute } from '@angular/router';
import Timeout = NodeJS.Timeout;

@Component({
  selector: 'jhi-start-quiz',
  templateUrl: './start-quiz.component.html',
  styleUrls: ['./start-quiz.component.scss'],
})
export class StartQuizComponent implements OnInit {
  code!: string;
  timeLimit!: number;
  interval!: Timeout;

  constructor(protected quizService: QuizService, protected route: ActivatedRoute) {}

  ngOnInit(): void {
    this.code = this.route.snapshot.paramMap.get('code') ?? '';
    this.timeLimit = 10;
    this.startTimer();
  }

  startTimer(): void {
    this.interval = setInterval(() => {
      if (this.timeLimit > 0) {
        this.timeLimit--;
      } else {
        this.timeLimit = 0;
      }
    }, 1000);
  }

  pauseTimer(): void {
    clearInterval(this.interval);
  }
}
