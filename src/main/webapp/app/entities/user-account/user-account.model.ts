import { IUser } from 'app/entities/user/user.model';
import { IQuestionAnswer } from 'app/entities/question-answer/question-answer.model';
import { IQuiz } from 'app/entities/quiz/quiz.model';
import { IQuestion } from 'app/entities/question/question.model';
import { IQuizResult } from 'app/entities/quiz-result/quiz-result.model';
import { IInvitation } from 'app/entities/invitation/invitation.model';

export interface IUserAccount {
  id?: number;
  rank?: number | null;
  quizesTaken?: number | null;
  quizzesCreated?: number | null;
  user?: IUser | null;
  questionAnswers?: IQuestionAnswer[] | null;
  quizzes?: IQuiz[] | null;
  questions?: IQuestion[] | null;
  quizResults?: IQuizResult[] | null;
  invitations?: IInvitation[] | null;
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
    public questions?: IQuestion[] | null,
    public quizResults?: IQuizResult[] | null,
    public invitations?: IInvitation[] | null
  ) {}
}

export function getUserAccountIdentifier(userAccount: IUserAccount): number | undefined {
  return userAccount.id;
}
