import { IQuestion } from 'app/entities/question/question.model';
import { IUserAccount } from 'app/entities/user-account/user-account.model';
import { IQuizRezult } from 'app/entities/quiz-rezult/quiz-rezult.model';
import { AnswerCode } from 'app/entities/enumerations/answer-code.model';

export interface IQuestionAnswer {
  id?: number;
  timeTaken?: number | null;
  success?: boolean | null;
  answer?: AnswerCode | null;
  question?: IQuestion | null;
  participant?: IUserAccount | null;
  rezult?: IQuizRezult | null;
}

export class QuestionAnswer implements IQuestionAnswer {
  constructor(
    public id?: number,
    public timeTaken?: number | null,
    public success?: boolean | null,
    public answer?: AnswerCode | null,
    public question?: IQuestion | null,
    public participant?: IUserAccount | null,
    public rezult?: IQuizRezult | null
  ) {
    this.success = this.success ?? false;
  }
}

export function getQuestionAnswerIdentifier(questionAnswer: IQuestionAnswer): number | undefined {
  return questionAnswer.id;
}
