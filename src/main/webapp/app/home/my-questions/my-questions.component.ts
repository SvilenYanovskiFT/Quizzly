import { Component, OnInit } from '@angular/core';
import { QuestionService } from 'app/entities/question/service/question.service';
import { QuestionCategoryService } from 'app/entities/question-category/service/question-category.service';
import { UserAccountService } from 'app/entities/user-account/service/user-account.service';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { IQuestionCategory } from 'app/entities/question-category/question-category.model';
import { IUserAccount } from 'app/entities/user-account/user-account.model';

@Component({
  selector: 'jhi-my-questions',
  templateUrl: './my-questions.component.html',
  styleUrls: ['./my-questions.component.scss'],
})
export class MyQuestionsComponent implements OnInit {
  myQuestions: any;
  isSaving = false;
  questionCategoriesSharedCollection: IQuestionCategory[] = [];
  userAccountsSharedCollection: IUserAccount[] = [];

  editForm = this.fb.group({
    id: [],
    sortOrder: [],
    text: [],
    image: [],
    answerA: [],
    answerB: [],
    answerC: [],
    answerD: [],
    correctAnswer: [],
    timeLimit: [],
    questionCategory: [],
    createdBy: [],
  });

  constructor(
    protected questionService: QuestionService,
    protected questionCategoryService: QuestionCategoryService,
    protected userAccountService: UserAccountService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.myQuestions = [
      { question: 'What is the name of the longest river in the world', a: 'Danube', b: 'Nile', c: 'Maritza', d: 'Iskar', correct: 'b' },
      { question: 'How many days do a martian year have?', a: '456', b: '341', c: '870', d: '125', correct: 'd' },
      {
        question: 'Who won the Oscar for a Best Director in 1967?',
        a: 'Me',
        b: 'Someone else',
        c: 'White american',
        d: 'Richard Gere',
        correct: 'a',
      },
      { question: 'How long is a nautical mile?', a: '1952 metres', b: '1564 metres', c: '1000 metres', d: '3251 metres', correct: 'd' },
      { question: 'What is the best Quiz app in the qorld?', a: 'Kahoot', b: 'Quizzly', c: 'I dont know', d: 'Who Cares?', correct: 'b' },
    ];
  }

  save(): void {
    console.log();
  }

  previousState(): void {
    console.log();
  }
}
