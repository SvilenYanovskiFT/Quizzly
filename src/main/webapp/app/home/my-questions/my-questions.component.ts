import { Component, OnInit } from '@angular/core';
import { QuestionService } from 'app/entities/question/service/question.service';

@Component({
  selector: 'jhi-my-questions',
  templateUrl: './my-questions.component.html',
  styleUrls: ['./my-questions.component.scss'],
})
export class MyQuestionsComponent implements OnInit {
  constructor(protected questionService: QuestionService) {}

  ngOnInit(): void {
    console.log();
  }
}
