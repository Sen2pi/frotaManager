export interface Trip {
  id?: number;
  vehicleId: number;
  driverId: number;
  startLocation: string;
  endLocation: string;
  startTime: Date;
  endTime?: Date;
  distance: number;
  fuelConsumption: number;
  status: 'PLANNED' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';
  notes?: string;
  createdAt?: Date;
  updatedAt?: Date;
} 