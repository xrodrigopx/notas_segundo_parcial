package com.example;

import java.util.*;

public class AlgoritmosModificados {

    // --------------------- DIJKSTRA CON ARISTAS BLOQUEADAS ---------------------
    public static int[] dijkstraConBloqueadas(int[][] grafo, boolean[][] bloqueadas, int origen) {
        int n = grafo.length;
        int[] dist = new int[n]; // distancias mínimas desde el origen
        boolean[] visitado = new boolean[n]; // nodos ya procesados
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[origen] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0])); // cola de prioridad ordenada por distancia
        pq.offer(new int[]{0, origen});

        while (!pq.isEmpty()) {
            int[] actual = pq.poll();
            int u = actual[1];
            if (visitado[u]) continue;
            visitado[u] = true;

            for (int v = 0; v < n; v++) {
                // esto es para ignorar las aristas bloqueadas
                if (!bloqueadas[u][v] && grafo[u][v] != Integer.MAX_VALUE) {
                    if (dist[v] > dist[u] + grafo[u][v]) {
                        dist[v] = dist[u] + grafo[u][v];
                        pq.offer(new int[]{dist[v], v});
                    }
                }
            }
        }
        return dist;
    }

    // --------------------- DIJKSTRA CON PARADA OBLIGATORIA ---------------------
    public static int[] dijkstraSimple(int[][] grafo, int origen) {
        int n = grafo.length;
        int[] dist = new int[n]; // distancias mínimas desde el origen
        boolean[] visitado = new boolean[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[origen] = 0;

        for (int i = 0; i < n; i++) {
            int u = -1;
            for (int j = 0; j < n; j++) {
                if (!visitado[j] && (u == -1 || dist[j] < dist[u])) u = j;
            }
            if (dist[u] == Integer.MAX_VALUE) break;
            visitado[u] = true;
            for (int v = 0; v < n; v++) {
                if (grafo[u][v] != Integer.MAX_VALUE && dist[v] > dist[u] + grafo[u][v]) {
                    dist[v] = dist[u] + grafo[u][v];
                }
            }
        }
        return dist;
    }

    public static int dijkstraConParada(int[][] grafo, int s, int v, int t) {
        // esto es para ejecutar Dijkstra dos veces: desde s a v y desde v a t
        int[] dist1 = dijkstraSimple(grafo, s);
        int[] dist2 = dijkstraSimple(grafo, v);
        return dist1[v] + dist2[t];
    }

    // --------------------- FLOYD CON CONTEO DE CAMINOS ---------------------
    public static int[][] floydConConteo(int[][] grafo, int[][] conteo) {
        int n = grafo.length;
        int[][] dist = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = grafo[i][j];
                conteo[i][j] = (grafo[i][j] != Integer.MAX_VALUE && i != j) ? 1 : 0; // esto es para iniciar el conteo de caminos mínimos
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE) {
                        if (dist[i][j] > dist[i][k] + dist[k][j]) {
                            dist[i][j] = dist[i][k] + dist[k][j];
                            conteo[i][j] = conteo[i][k] * conteo[k][j]; // esto es para actualizar el conteo si se encontró un nuevo camino mínimo
                        } else if (dist[i][j] == dist[i][k] + dist[k][j]) {
                            conteo[i][j] += conteo[i][k] * conteo[k][j]; // esto es para contar caminos adicionales con igual costo
                        }
                    }
                }
            }
        }
        return dist;
    }

    // --------------------- DETECTAR NODOS CRÍTICOS ---------------------
    public static List<Integer> nodosCriticos(int[][] grafo) {
        int n = grafo.length;
        int[][] base = floyd(grafo);
        List<Integer> criticos = new ArrayList<>();

        for (int nodo = 0; nodo < n; nodo++) {
            int[][] copia = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    copia[i][j] = (i == nodo || j == nodo) ? Integer.MAX_VALUE : grafo[i][j]; // esto es para quitar el nodo de la matriz
                }
            }
            int[][] dist = floyd(copia);
            outer:
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][j] > base[i][j]) {
                        criticos.add(nodo); // esto es para identificar si al quitar el nodo aumentan las distancias mínimas
                        break outer;
                    }
                }
            }
        }
        return criticos;
    }

    // --------------------- FLOYD BÁSICO ---------------------
    public static int[][] floyd(int[][] grafo) {
        int n = grafo.length;
        int[][] dist = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(grafo[i], 0, dist[i], 0, n);
        }
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE
                            && dist[i][j] > dist[i][k] + dist[k][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }
        return dist;
    }

    // --------------------- IMPACTO DE QUITAR ARISTAS ---------------------
    public static int[] aristaMayorImpacto(int[][] grafo) {
        int n = grafo.length;
        int[][] base = floyd(grafo);
        int maxDif = 0;
        int[] peorArista = {-1, -1};

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grafo[i][j] != Integer.MAX_VALUE) {
                    int original = grafo[i][j];
                    grafo[i][j] = Integer.MAX_VALUE; // esto es para simular la eliminación temporal de la arista
                    int[][] nueva = floyd(grafo);
                    for (int x = 0; x < n; x++) {
                        for (int y = 0; y < n; y++) {
                            if (base[x][y] != Integer.MAX_VALUE) {
                                int dif = nueva[x][y] - base[x][y];
                                if (dif > maxDif) {
                                    maxDif = dif;
                                    peorArista[0] = i;
                                    peorArista[1] = j;
                                }
                            }
                        }
                    }
                    grafo[i][j] = original; // restaurar la arista original
                }
            }
        }
        return peorArista;
    }

}
