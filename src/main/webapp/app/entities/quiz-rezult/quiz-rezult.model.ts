import { IQuestionAnswer } from 'app/entities/question-answer/question-answer.model';
import { IQuiz } from 'app/entities/quiz/quiz.model';
import { IUserAccount } from 'app/entities/user-account/user-account.model';

export interface IQuizRezult {
  id?: number;
  score?: number | null;
  rank?: number | null;
  questionAnswers?: IQuestionAnswer[] | null;
  quiz?: IQuiz | null;
  quizRezult?: IUserAccount | null;
}

export class QuizRezult implements IQuizRezult {
  constructor(
    public id?: number,
    public score?: number | null,
    public rank?: number | null,
    public questionAnswers?: IQuestionAnswer[] | null,
    public quiz?: IQuiz | null,
    public quizRezult?: IUserAccount | null
  ) {}
}

export function getQuizRezultIdentifier(quizRezult: IQuizRezult): number | undefined {
  return quizRezult.id;
}
