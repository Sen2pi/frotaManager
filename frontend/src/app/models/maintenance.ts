export interface Maintenance {
  id?: number;
  vehicleId: number;
  type: 'PREVENTIVE' | 'CORRECTIVE' | 'INSPECTION';
  description: string;
  cost: number;
  status: 'SCHEDULED' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';
  scheduledDate: Date;
  completedDate?: Date;
  mechanicName?: string;
  notes?: string;
  createdAt?: Date;
  updatedAt?: Date;
} 