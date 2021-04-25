import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IQuestionCategory } from '../question-category.model';
import { QuestionCategoryService } from '../service/question-category.service';
import { QuestionCategoryDeleteDialogComponent } from '../delete/question-category-delete-dialog.component';

@Component({
  selector: 'jhi-question-category',
  templateUrl: './question-category.component.html',
})
export class QuestionCategoryComponent implements OnInit {
  questionCategories?: IQuestionCategory[];
  isLoading = false;

  constructor(protected questionCategoryService: QuestionCategoryService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.questionCategoryService.query().subscribe(
      (res: HttpResponse<IQuestionCategory[]>) => {
        this.isLoading = false;
        this.questionCategories = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IQuestionCategory): number {
    return item.id!;
  }

  delete(questionCategory: IQuestionCategory): void {
    const modalRef = this.modalService.open(QuestionCategoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.questionCategory = questionCategory;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
