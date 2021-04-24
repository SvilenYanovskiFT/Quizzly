import { IQuestionAnswer } from 'app/entities/question-answer/question-answer.model';
import { IQuiz } from 'app/entities/quiz/quiz.model';
import { IUserAccount } from 'app/entities/user-account/user-account.model';

export interface IQuizResult {
  id?: number;
  score?: number | null;
  rank?: number | null;
  questionAnswers?: IQuestionAnswer[] | null;
  quiz?: IQuiz | null;
  quizResult?: IUserAccount | null;
}

export class QuizResult implements IQuizResult {
  constructor(
    public id?: number,
    public score?: number | null,
    public rank?: number | null,
    public questionAnswers?: IQuestionAnswer[] | null,
    public quiz?: IQuiz | null,
    public quizResult?: IUserAccount | null
  ) {}
}

export function getQuizResultIdentifier(quizResult: IQuizResult): number | undefined {
  return quizResult.id;
}
