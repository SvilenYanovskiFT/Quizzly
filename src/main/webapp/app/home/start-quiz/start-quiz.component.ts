import { Component, OnInit } from '@angular/core';
import { QuizService } from 'app/entities/quiz/service/quiz.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-start-quiz',
  templateUrl: './start-quiz.component.html',
  styleUrls: ['./start-quiz.component.scss'],
})
export class StartQuizComponent implements OnInit {
  code!: string;

  constructor(protected quizService: QuizService, protected route: ActivatedRoute) {}

  ngOnInit(): void {
    this.code = this.route.snapshot.paramMap.get('code') ?? '';
  }
}
