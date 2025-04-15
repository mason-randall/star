export interface StarPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
