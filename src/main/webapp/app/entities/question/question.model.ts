import { IQuestionAnswer } from 'app/entities/question-answer/question-answer.model';
import { IQuiz } from 'app/entities/quiz/quiz.model';
import { AnswerCode } from 'app/entities/enumerations/answer-code.model';

export interface IQuestion {
  id?: number;
  sortOrder?: number | null;
  text?: string | null;
  image?: string | null;
  answerA?: string | null;
  answerB?: string | null;
  answerC?: string | null;
  answerD?: string | null;
  correctAnswer?: AnswerCode | null;
  timeLimit?: number | null;
  questionAnswers?: IQuestionAnswer[] | null;
  quizzes?: IQuiz[] | null;
}

export class Question implements IQuestion {
  constructor(
    public id?: number,
    public sortOrder?: number | null,
    public text?: string | null,
    public image?: string | null,
    public answerA?: string | null,
    public answerB?: string | null,
    public answerC?: string | null,
    public answerD?: string | null,
    public correctAnswer?: AnswerCode | null,
    public timeLimit?: number | null,
    public questionAnswers?: IQuestionAnswer[] | null,
    public quizzes?: IQuiz[] | null
  ) {}
}

export function getQuestionIdentifier(question: IQuestion): number | undefined {
  return question.id;
}
