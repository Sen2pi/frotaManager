export interface Vehicle {
  id?: number;
  brand: string;
  model: string;
  licensePlate: string;
  year: number;
  fuelType: 'GASOLINE' | 'DIESEL' | 'ELECTRIC' | 'HYBRID';
  status: 'AVAILABLE' | 'IN_USE' | 'MAINTENANCE' | 'OUT_OF_SERVICE';
  mileage: number;
  lastMaintenance?: Date;
  nextMaintenance?: Date;
  driverId?: number;
  createdAt?: Date;
  updatedAt?: Date;
} 