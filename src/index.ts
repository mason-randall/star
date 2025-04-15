import { registerPlugin } from '@capacitor/core';

import type { StarPlugin } from './definitions';

const Star = registerPlugin<StarPlugin>('Star', {
  web: () => import('./web').then((m) => new m.StarWeb()),
});

export * from './definitions';
export { Star };
