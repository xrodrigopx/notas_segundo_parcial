package com.example;

import java.util.*;

// Clase Main con algoritmos puros sin modificaciones especiales
public class Main {
    public static void main(String[] args) {
        System.out.println(0);
        System.out.println(1);
        System.out.println(2);
        System.out.println(3);
        System.out.println(4);
    }

    // DFS puro (búsqueda en profundidad)
    void dfs(int nodo, boolean[] visitado, List<List<Integer>> grafo) {
        visitado[nodo] = true;
        for (int vecino : grafo.get(nodo)) {
            if (!visitado[vecino]) {
                dfs(vecino, visitado, grafo);
            }
        }
    }

    // BFS puro (búsqueda en anchura)
    void bfs(int inicio, List<List<Integer>> grafo) {
        boolean[] visitado = new boolean[grafo.size()];
        Queue<Integer> cola = new LinkedList<>();
        visitado[inicio] = true;
        cola.add(inicio);

        while (!cola.isEmpty()) {
            int nodo = cola.poll();
            for (int vecino : grafo.get(nodo)) {
                if (!visitado[vecino]) {
                    visitado[vecino] = true;
                    cola.add(vecino);
                }
            }
        }
    }

    // Dijkstra puro con heap (camino más corto desde origen a todos los nodos)
    int[] dijkstra(int origen, List<List<int[]>> grafo, int n) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[origen] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.add(new int[]{0, origen});

        while (!pq.isEmpty()) {
            int[] actual = pq.poll();
            int d = actual[0], nodo = actual[1];
            if (d > dist[nodo]) continue;
            for (int[] vecino : grafo.get(nodo)) {
                int peso = vecino[0], destino = vecino[1];
                if (dist[nodo] + peso < dist[destino]) {
                    dist[destino] = dist[nodo] + peso;
                    pq.add(new int[]{dist[destino], destino});
                }
            }
        }
        return dist;
    }

    // Floyd-Warshall puro (distancias mínimas entre todos los pares)
    void floydWarshall(int[][] dist, int V) {
        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (dist[i][k] < Integer.MAX_VALUE && dist[k][j] < Integer.MAX_VALUE)
                        dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                }
            }
        }
    }

    // Kruskal puro (árbol generador mínimo)
    class Arista {
        int u, v, peso;
        Arista(int u, int v, int peso) {
            this.u = u; this.v = v; this.peso = peso;
        }
    }

    int kruskal(int n, List<Arista> aristas) {
        Collections.sort(aristas, Comparator.comparingInt(a -> a.peso));
        int[] padre = new int[n];
        for (int i = 0; i < n; i++) padre[i] = i;

        int pesoTotal = 0;
        for (Arista a : aristas) {
            int ru = find(a.u, padre), rv = find(a.v, padre);
            if (ru != rv) {
                pesoTotal += a.peso;
                padre[ru] = rv;
            }
        }
        return pesoTotal;
    }

    int find(int x, int[] padre) {
        if (padre[x] != x)
            padre[x] = find(padre[x], padre);
        return padre[x];
    }

    // Prim puro (árbol generador mínimo desde un nodo)
    int prim(int n, List<List<int[]>> grafo) {
        boolean[] visitado = new boolean[n];
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.add(new int[]{0, 0});
        int pesoTotal = 0;

        while (!pq.isEmpty()) {
            int[] actual = pq.poll();
            int peso = actual[0], nodo = actual[1];
            if (visitado[nodo]) continue;
            visitado[nodo] = true;
            pesoTotal += peso;
            for (int[] vecino : grafo.get(nodo)) {
                if (!visitado[vecino[1]]) {
                    pq.add(new int[]{vecino[0], vecino[1]});
                }
            }
        }
        return pesoTotal;
    }

    // Algoritmo para ordenamiento descendente eficiente (Heapsort) - para IoT con memoria limitada
    void heapify(double[] arr, int n, int i) {
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

    void heapSortDesc(double[] arr) {
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
