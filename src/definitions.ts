export interface StarPlugin {
  print(options: { value: string }): Promise<{ success: boolean }>;
}
