import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  email = '';
  password = '';
  loading = false;
  hidePassword = true;

  // Credenciais de demonstração
  demoCredentials = [
    { role: 'Driver', email: 'test@test.com', password: '123456', color: '#38a169' }
  ];

  constructor(
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  onLogin(): void {
    if (!this.email || !this.password) {
      this.showSnackBar('Por favor, preencha todos os campos', 'error');
      return;
    }

    console.log('🚀 Frontend: Sending login request');
    console.log('📧 Email:', this.email);
    console.log('🔑 Password length:', this.password.length);
    console.log('🔑 Password:', this.password); // Temporary debug - remove in production

    this.loading = true;
    
    this.authService.login(this.email, this.password).subscribe({
      next: (response) => {
        this.loading = false;
        console.log('✅ Login success:', response);
        this.showSnackBar('Login realizado com sucesso!', 'success');
        this.router.navigate(['/dashboard']);
      },
      error: (error) => {
        this.loading = false;
        console.error('❌ Erro no login:', error);
        console.error('❌ Error details:', error.error);
        this.showSnackBar('Erro no login. Verifique suas credenciais.', 'error');
      }
    });
  }



  onDemoLogin(credential: any): void {
    this.email = credential.email;
    this.password = credential.password;
    this.showSnackBar(`Credenciais de ${credential.role} preenchidas!`, 'info');
  }

  private showSnackBar(message: string, type: 'success' | 'error' | 'info' = 'info'): void {
    this.snackBar.open(message, 'Fermer', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom',
      panelClass: type === 'error' ? 'error-snackbar' : type === 'success' ? 'success-snackbar' : 'info-snackbar'
    });
  }
} 