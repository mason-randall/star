export interface StarPlugin {
  print(options: { value: string, copies?: number }): Promise<{ success: boolean }>;
}
