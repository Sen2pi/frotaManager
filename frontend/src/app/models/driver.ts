export interface Driver {
  id?: number;
  name: string;
  licenseNumber: string;
  licenseType: 'A' | 'B' | 'C' | 'D' | 'E';
  status: 'AVAILABLE' | 'ON_TRIP' | 'OFF_DUTY' | 'SUSPENDED';
  phoneNumber: string;
  email: string;
  hireDate: Date;
  vehicleId?: number;
  createdAt?: Date;
  updatedAt?: Date;
} 