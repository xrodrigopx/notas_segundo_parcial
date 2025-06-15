package com.example;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        System.out.println(0);
        System.out.println(1);
        System.out.println(2);
        System.out.println(3);
        System.out.println(4);
    }
    // DFS - Búsqueda en profundidad
// Esta función recorre el grafo empezando desde un nodo, y va tan profundo como puede antes de retroceder
void dfs(int nodo, boolean[] visitado, List<List<Integer>> grafo) {
    visitado[nodo] = true; // marcamos el nodo como visitado
    for (int vecino : grafo.get(nodo)) { // recorremos todos los vecinos del nodo actual
        if (!visitado[vecino]) { // si el vecino no fue visitado todavía
            dfs(vecino, visitado, grafo); // lo visitamos recursivamente
        }
    }
}

// BFS - Búsqueda en anchura
// Recorre el grafo por niveles, ideal para encontrar caminos más cortos en grafos no ponderados
void bfs(int inicio, List<List<Integer>> grafo) {
    boolean[] visitado = new boolean[grafo.size()];
    Queue<Integer> cola = new LinkedList<>(); // usamos una cola para el recorrido en anchura
    visitado[inicio] = true;
    cola.add(inicio);

    while (!cola.isEmpty()) {
        int nodo = cola.poll(); // sacamos el primero de la cola
        for (int vecino : grafo.get(nodo)) {
            if (!visitado[vecino]) {
                visitado[vecino] = true; // marcamos como visitado
                cola.add(vecino); // y lo agregamos a la cola
            }
        }
    }
}

// Dijkstra
// Busca la distancia mínima desde un nodo origen hacia todos los demás, sin usar pesos negativos
int[] dijkstra(int origen, List<List<int[]>> grafo, int n) {
    int[] dist = new int[n];
    Arrays.fill(dist, Integer.MAX_VALUE); // inicializamos todo en infinito
    dist[origen] = 0;
    PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
    pq.add(new int[]{0, origen}); // agregamos el origen con distancia 0

    while (!pq.isEmpty()) {
        int[] actual = pq.poll(); // sacamos el nodo más cercano
        int d = actual[0], nodo = actual[1];
        if (d > dist[nodo]) continue; // si ya tenemos una mejor distancia, ignoramos
        for (int[] vecino : grafo.get(nodo)) {
            int peso = vecino[0], destino = vecino[1];
            if (dist[nodo] + peso < dist[destino]) {
                dist[destino] = dist[nodo] + peso; // actualizamos la distancia
                pq.add(new int[]{dist[destino], destino}); // lo agregamos a la cola para seguir
            }
        }
    }
    return dist;
}

// Floyd-Warshall
// Calcula la distancia mínima entre todos los pares de nodos
void floydWarshall(int[][] dist, int V) {
    for (int k = 0; k < V; k++) { // probamos con cada nodo como intermedio
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                // si pasar por k mejora el camino entre i y j, lo actualizamos
                if (dist[i][k] < Integer.MAX_VALUE && dist[k][j] < Integer.MAX_VALUE)
                    dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
            }
        }
    }
}

// Kruskal
// Construye un árbol de expansión mínima usando unión de conjuntos
class Arista {
    int u, v, peso;
    Arista(int u, int v, int peso) {
        this.u = u; this.v = v; this.peso = peso;
    }
}

int kruskal(int n, List<Arista> aristas) {
    Collections.sort(aristas, Comparator.comparingInt(a -> a.peso)); // ordenamos por peso
    int[] padre = new int[n];
    for (int i = 0; i < n; i++) padre[i] = i; // cada nodo es su propio conjunto al principio

    int pesoTotal = 0;
    for (Arista a : aristas) {
        int ru = find(a.u, padre), rv = find(a.v, padre);
        if (ru != rv) { // si no forman ciclo
            pesoTotal += a.peso; // agregamos su peso
            padre[ru] = rv; // unimos los conjuntos
        }
    }
    return pesoTotal;
}

int find(int x, int[] padre) {
    if (padre[x] != x)
        padre[x] = find(padre[x], padre); // compresión de caminos
    return padre[x];
}

// Prim
// Otra forma de obtener el árbol de expansión mínima, arrancando desde un nodo y creciendo
int prim(int n, List<List<int[]>> grafo) {
    boolean[] visitado = new boolean[n];
    PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
    pq.add(new int[]{0, 0}); // arrancamos desde el nodo 0 con peso 0
    int pesoTotal = 0;

    while (!pq.isEmpty()) {
        int[] actual = pq.poll();
        int peso = actual[0], nodo = actual[1];
        if (visitado[nodo]) continue; // si ya lo usamos, salteamos
        visitado[nodo] = true;
        pesoTotal += peso; // sumamos el peso al total del árbol
        for (int[] vecino : grafo.get(nodo)) {
            if (!visitado[vecino[1]]) {
                pq.add(new int[]{vecino[0], vecino[1]}); // agregamos los vecinos a la cola
            }
        }
    }
    return pesoTotal;
}

}