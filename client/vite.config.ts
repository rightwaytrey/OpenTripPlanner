import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  base: '/',
  server: {
    port: 9966,
    host: '0.0.0.0',
  },
  build: {
    outDir: 'output',
    emptyOutDir: true,
  },
  // @ts-ignore
  test: {
    environment: 'jsdom',
  },
});
