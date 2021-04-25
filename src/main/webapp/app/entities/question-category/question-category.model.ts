import { IQuestion } from 'app/entities/question/question.model';

export interface IQuestionCategory {
  id?: number;
  name?: string | null;
  categories?: IQuestion[] | null;
}

export class QuestionCategory implements IQuestionCategory {
  constructor(public id?: number, public name?: string | null, public categories?: IQuestion[] | null) {}
}

export function getQuestionCategoryIdentifier(questionCategory: IQuestionCategory): number | undefined {
  return questionCategory.id;
}
