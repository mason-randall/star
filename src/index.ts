import { registerPlugin } from '@capacitor/core';

import type { StarPlugin } from './definitions';

const Star = registerPlugin<StarPlugin>('Star', {
});

export * from './definitions';
export { Star };
