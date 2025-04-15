import { WebPlugin } from '@capacitor/core';

import type { StarPlugin } from './definitions';

export class StarWeb extends WebPlugin implements StarPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
