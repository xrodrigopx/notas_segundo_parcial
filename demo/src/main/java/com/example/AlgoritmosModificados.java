package com.example;

import java.util.*;

public class AlgoritmosModificados {

    // Dijkstra con aristas bloqueadas (modificado para ignorar ciertas aristas)
    public static int[] dijkstraConBloqueadas(int[][] grafo, boolean[][] bloqueadas, int origen) {
        int n = grafo.length;
        int[] dist = new int[n];
        boolean[] visitado = new boolean[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[origen] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, origen});

        while (!pq.isEmpty()) {
            int[] actual = pq.poll();
            int u = actual[1];
            if (visitado[u]) continue;
            visitado[u] = true;

            for (int v = 0; v < n; v++) {
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

    // Dijkstra simple (sin heap, modificado para parada obligatoria)
    public static int[] dijkstraSimple(int[][] grafo, int origen) {
        int n = grafo.length;
        int[] dist = new int[n];
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

    // Dijkstra con parada obligatoria (camino s->v + v->t)
    public static int dijkstraConParada(int[][] grafo, int s, int v, int t) {
        int[] dist1 = dijkstraSimple(grafo, s);
        int[] dist2 = dijkstraSimple(grafo, v);
        return dist1[v] + dist2[t];
    }

    // Floyd con conteo de caminos mínimos
    public static int[][] floydConConteo(int[][] grafo, int[][] conteo) {
        int n = grafo.length;
        int[][] dist = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = grafo[i][j];
                conteo[i][j] = (grafo[i][j] != Integer.MAX_VALUE && i != j) ? 1 : 0;
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE) {
                        if (dist[i][j] > dist[i][k] + dist[k][j]) {
                            dist[i][j] = dist[i][k] + dist[k][j];
                            conteo[i][j] = conteo[i][k] * conteo[k][j];
                        } else if (dist[i][j] == dist[i][k] + dist[k][j]) {
                            conteo[i][j] += conteo[i][k] * conteo[k][j];
                        }
                    }
                }
            }
        }
        return dist;
    }

    // Detectar nodos críticos (si al quitar un nodo aumentan distancias mínimas)
    public static List<Integer> nodosCriticos(int[][] grafo) {
        int n = grafo.length;
        int[][] base = floyd(grafo);
        List<Integer> criticos = new ArrayList<>();

        for (int nodo = 0; nodo < n; nodo++) {
            int[][] copia = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    copia[i][j] = (i == nodo || j == nodo) ? Integer.MAX_VALUE : grafo[i][j];
                }
            }
            int[][] dist = floyd(copia);
            outer:
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][j] > base[i][j]) {
                        criticos.add(nodo);
                        break outer;
                    }
                }
            }
        }
        return criticos;
    }

    // Floyd básico para uso interno
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

    // Arista con mayor impacto (cuál al eliminar incrementa más las distancias mínimas)
    public static int[] aristaMayorImpacto(int[][] grafo) {
        int n = grafo.length;                                   
        int[][] base = floyd(grafo);
        int maxDif = 0;
        int[] peorArista = {-1, -1};

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grafo[i][j] != Integer.MAX_VALUE) {
                    int original = grafo[i][j];
                    grafo[i][j] = Integer.MAX_VALUE;
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
                    grafo[i][j] = original;
                }
            }
        }
        return peorArista;
    }

    // DFS modificado para caminos sin conectar nodos de tipo "procesamiento"
    public static void dfsModificado(int nodo, int destino, boolean[] visitado, List<List<Integer>> grafo,
                                    List<Integer> caminoActual, List<List<Integer>> caminos, Set<Integer> nodosProcesamiento) {
        visitado[nodo] = true;
        caminoActual.add(nodo);

        if (nodo == destino) {
            caminos.add(new ArrayList<>(caminoActual));
        } else {
            for (int vecino : grafo.get(nodo)) {
                if (!visitado[vecino] && !(nodosProcesamiento.contains(nodo) && nodosProcesamiento.contains(vecino))) {
                    dfsModificado(vecino, destino, visitado, grafo, caminoActual, caminos, nodosProcesamiento);
                }
            }
        }

        caminoActual.remove(caminoActual.size() - 1);
        visitado[nodo] = false;
    }

    // Ordenamiento heapSort descendente para IoT con memoria limitada
    public static void heapify(double[] arr, int n, int i) {
        int largest = i;
        int l = 2*i + 1;
        int r = 2*i + 2;

        if (l < n && arr[l] > arr[largest]) largest = l;
        if (r < n && arr[r] > arr[largest]) largest = r;

        if (largest != i) {
            double temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;
            heapify(arr, n, largest);
        }
    }

    public static void heapSortDesc(double[] arr) {
        int n = arr.length;
        for (int i = n/2 -1; i >= 0; i--) {
            heapify(arr, n, i);
        }
        for (int i = n -1; i > 0; i--) {
            double temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            heapify(arr, i, 0);
        }
    }
}
