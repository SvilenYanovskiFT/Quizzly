import { IQuizResult } from 'app/entities/quiz-result/quiz-result.model';
import { IQuestion } from 'app/entities/question/question.model';
import { IUserAccount } from 'app/entities/user-account/user-account.model';
import { QuizType } from 'app/entities/enumerations/quiz-type.model';

export interface IQuiz {
  id?: number;
  name?: string | null;
  code?: string | null;
  quizType?: QuizType | null;
  quizResults?: IQuizResult[] | null;
  questions?: IQuestion[] | null;
  owner?: IUserAccount | null;
}

export class Quiz implements IQuiz {
  constructor(
    public id?: number,
    public name?: string | null,
    public code?: string | null,
    public quizType?: QuizType | null,
    public quizResults?: IQuizResult[] | null,
    public questions?: IQuestion[] | null,
    public owner?: IUserAccount | null
  ) {}
}

export function getQuizIdentifier(quiz: IQuiz): number | undefined {
  return quiz.id;
}
