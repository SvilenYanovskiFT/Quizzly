import { IQuestion } from 'app/entities/question/question.model';
import { IQuizRezult } from 'app/entities/quiz-rezult/quiz-rezult.model';
import { IUserAccount } from 'app/entities/user-account/user-account.model';
import { QuizType } from 'app/entities/enumerations/quiz-type.model';

export interface IQuiz {
  id?: number;
  name?: string | null;
  code?: string | null;
  quizType?: QuizType | null;
  questions?: IQuestion[] | null;
  quizRezults?: IQuizRezult[] | null;
  owner?: IUserAccount | null;
}

export class Quiz implements IQuiz {
  constructor(
    public id?: number,
    public name?: string | null,
    public code?: string | null,
    public quizType?: QuizType | null,
    public questions?: IQuestion[] | null,
    public quizRezults?: IQuizRezult[] | null,
    public owner?: IUserAccount | null
  ) {}
}

export function getQuizIdentifier(quiz: IQuiz): number | undefined {
  return quiz.id;
}
