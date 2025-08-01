import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ReportService } from '../../services/report.service';

@Component({
  selector: 'app-report',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss']
})
export class ReportComponent implements OnInit {
  loading = signal(false);
  error = signal('');
  reportData = signal<any>(null);

  constructor(private reportService: ReportService) { }

  ngOnInit(): void {
    this.loadReportData();
  }

  loadReportData(): void {
    this.loading.set(true);
    this.error.set('');
    this.reportService.getReportData().subscribe({
      next: (data) => {
        this.reportData.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Failed to load report data.');
        this.loading.set(false);
        console.error(err);
      }
    });
  }

  printReport(): void {
    window.print();
  }
}