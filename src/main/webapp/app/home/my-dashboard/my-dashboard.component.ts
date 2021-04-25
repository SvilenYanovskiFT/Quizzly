import { Component, OnInit } from '@angular/core';
import { QuizService } from 'app/entities/quiz/service/quiz.service';

@Component({
  selector: 'jhi-my-dashboard',
  templateUrl: './my-dashboard.component.html',
  styleUrls: ['./my-dashboard.component.scss'],
})
export class MyDashboardComponent implements OnInit {
  quizzesTaken: any;
  topPlayers: any;
  invitations: any;
  constructor(protected quizService: QuizService) {}

  ngOnInit(): void {
    this.quizzesTaken = [
      { code: 'WE3R', players: 7, score: 45, rank: 117 },
      { code: 'T1FG', players: 11, score: 67, rank: 108 },
      { code: 'HT2W', players: 4, score: 70, rank: 100 },
      { code: 'KJ8U', players: 8, score: 25, rank: 157 },
      { code: 'GG6T', players: 18, score: 76, rank: 124 },
      { code: 'MN8U', players: 3, score: 55, rank: 138 },
      { code: 'J89K', players: 5, score: 69, rank: 131 },
    ];

    this.topPlayers = [
      { login: 'ivan.ivanov@xoomworks.com', quizzes: 57, avg: 90, rank: 1 },
      { login: 'pesho.peshev@xoomworks.com', quizzes: 31, avg: 86, rank: 2 },
      { login: 'gosho.goshev@xoomworks.com', quizzes: 24, avg: 85, rank: 3 },
      { login: 'maria.pesheva@xoomworks.com', quizzes: 78, avg: 80, rank: 4 },
      { login: 'pepa.pepova@xoomworks.com', quizzes: 118, avg: 79, rank: 5 },
      { login: 'goshka.baba@xoomworks.com', quizzes: 34, avg: 77, rank: 6 },
      { login: 'kiro.tzar@xoomworks.com', quizzes: 65, avg: 75, rank: 7 },
      { login: 'gosho.thebrain@xoomworks.com', quizzes: 45, avg: 74, rank: 8 },
      { login: 'will.iam@xoomworks.com', quizzes: 25, avg: 74, rank: 9 },
      { login: 'svilen.yanovski@xoomworks.com', quizzes: 68, avg: 69, rank: 10 },
    ];

    this.invitations = [
      { invBy: 'ivan.ivanov@xoomworks.com', code: 'WE3R', response: 'rejected', score: '' },
      { invBy: 'maria.pesheva@xoomworks.com', code: 'TY6T', response: 'accepted', score: 76 },
      { invBy: 'svilen.yanovski@xoomworks.com', code: 'HH6T', response: 'accepted', score: 56 },
      { invBy: 'pepa.pepova@xoomworks.com', code: 'GH44', response: 'accepted', score: 54 },
      { invBy: 'gosho.goshev@xoomworks.com', code: 'SD43', response: 'rejected', score: '' },
      { invBy: 'ivan.ivanov@xoomworks.com', code: 'WE3R', response: 'rejected', score: '' },
      { invBy: 'maria.pesheva@xoomworks.com', code: 'TY6T', response: 'accepted', score: 76 },
      { invBy: 'svilen.yanovski@xoomworks.com', code: 'HH6T', response: 'accepted', score: 56 },
      { invBy: 'pepa.pepova@xoomworks.com', code: 'GH44', response: 'accepted', score: 54 },
      { invBy: 'gosho.goshev@xoomworks.com', code: 'SD43', response: 'rejected', score: '' },
    ];
  }
}
