import { WebPlugin } from '@capacitor/core';

import type { StarPlugin } from './definitions';

export class StarWeb extends WebPlugin implements StarPlugin {
  async print(options: { value: string }): Promise<{ success: boolean }> {
    return { success: false };
  }
}
