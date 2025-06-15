package com.example;

import org.junit.jupiter.api.Test;

import com.example.Main.Arista;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class MainTest {
    private Main main = new Main();

    @Test
    void testDFS() {
        // Crear un grafo simple: 0 -> 1 -> 2
        List<List<Integer>> grafo = new ArrayList<>();
        grafo.add(Arrays.asList(1));     // nodo 0
        grafo.add(Arrays.asList(2));     // nodo 1
        grafo.add(new ArrayList<>());    // nodo 2

        boolean[] visitado = new boolean[3];
        main.dfs(0, visitado, grafo);

        // Verificar que todos los nodos fueron visitados
        assertTrue(visitado[0]);
        assertTrue(visitado[1]);
        assertTrue(visitado[2]);
    }

    @Test
    void testBFS() {
        // Crear un grafo simple: 0 -> 1 -> 2
        List<List<Integer>> grafo = new ArrayList<>();
        grafo.add(Arrays.asList(1));     // nodo 0
        grafo.add(Arrays.asList(2));     // nodo 1
        grafo.add(new ArrayList<>());    // nodo 2

        main.bfs(0, grafo);
        // BFS no retorna nada, pero podemos verificar que no lanza excepciones
    }

    @Test
    void testDijkstra() {
        // Crear un grafo ponderado
        List<List<int[]>> grafo = new ArrayList<>();
        grafo.add(Arrays.asList(new int[]{1, 1}, new int[]{4, 2}));  // nodo 0
        grafo.add(Arrays.asList(new int[]{2, 2}));                    // nodo 1
        grafo.add(Arrays.asList(new int[]{3, 3}));                    // nodo 2
        grafo.add(new ArrayList<>());                                 // nodo 3
        grafo.add(Arrays.asList(new int[]{1, 3}));                    // nodo 4

        int[] distancias = main.dijkstra(0, grafo, 5);

        // Verificar las distancias mínimas
        assertEquals(0, distancias[0]);
        assertEquals(1, distancias[1]);
        assertEquals(3, distancias[2]);
        assertEquals(5, distancias[3]);
        assertEquals(2, distancias[4]);
    }

    @Test
    void testFloydWarshall() {
        // Crear matriz de distancias
        int[][] dist = new int[4][4];
        for (int i = 0; i < 4; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
            dist[i][i] = 0;
        }
        
        // Agregar algunas aristas
        dist[0][1] = 3;
        dist[0][2] = 6;
        dist[1][2] = 2;
        dist[2][3] = 1;

        main.floydWarshall(dist, 4);

        // Verificar algunas distancias mínimas
        assertEquals(3, dist[0][1]);
        assertEquals(5, dist[0][2]);
        assertEquals(6, dist[0][3]);
    }

    @Test
    void testKruskal() {
        List<Arista> aristas = new ArrayList<>();
        aristas.add(main.new Arista(0, 1, 2));
        aristas.add(main.new Arista(1, 2, 3));
        aristas.add(main.new Arista(2, 3, 1));
        aristas.add(main.new Arista(0, 3, 4));

        int pesoTotal = main.kruskal(4, aristas);
        assertEquals(6, pesoTotal); // 2 + 3 + 1 = 6
    }

    @Test
    void testPrim() {
        List<List<int[]>> grafo = new ArrayList<>();
        grafo.add(Arrays.asList(new int[]{2, 1}, new int[]{4, 3}));  // nodo 0
        grafo.add(Arrays.asList(new int[]{2, 0}, new int[]{3, 2}));  // nodo 1
        grafo.add(Arrays.asList(new int[]{3, 1}, new int[]{1, 3}));  // nodo 2
        grafo.add(Arrays.asList(new int[]{4, 0}, new int[]{1, 2}));  // nodo 3

        int pesoTotal = main.prim(4, grafo);
        assertEquals(6, pesoTotal); // 2 + 3 + 1 = 6
    }
} 