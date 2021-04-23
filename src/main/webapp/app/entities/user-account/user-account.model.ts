import { IUser } from 'app/entities/user/user.model';
import { IQuestionAnswer } from 'app/entities/question-answer/question-answer.model';
import { IQuiz } from 'app/entities/quiz/quiz.model';
import { IQuizRezult } from 'app/entities/quiz-rezult/quiz-rezult.model';

export interface IUserAccount {
  id?: number;
  rank?: number | null;
  quizesTaken?: number | null;
  quizzesCreated?: number | null;
  user?: IUser | null;
  questionAnswers?: IQuestionAnswer[] | null;
  quizzes?: IQuiz[] | null;
  quizRezults?: IQuizRezult[] | null;
}

export class UserAccount implements IUserAccount {
  constructor(
    public id?: number,
    public rank?: number | null,
    public quizesTaken?: number | null,
    public quizzesCreated?: number | null,
    public user?: IUser | null,
    public questionAnswers?: IQuestionAnswer[] | null,
    public quizzes?: IQuiz[] | null,
    public quizRezults?: IQuizRezult[] | null
  ) {}
}

export function getUserAccountIdentifier(userAccount: IUserAccount): number | undefined {
  return userAccount.id;
}
