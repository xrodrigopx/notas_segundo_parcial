package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class AlgoritmosModificadosTest {

    @Test
    void testDijkstraConBloqueadas() {
        int INF = Integer.MAX_VALUE;
        int[][] grafo = {
            {0, 1, INF},
            {INF, 0, 2},
            {INF, INF, 0}
        };
        boolean[][] bloqueadas = {
            {false, false, false},
            {false, false, true},  // bloqueamos arista 1->2
            {false, false, false}
        };
        int[] dist = AlgoritmosModificados.dijkstraConBloqueadas(grafo, bloqueadas, 0);
        assertEquals(0, dist[0]);
        assertEquals(1, dist[1]);
        assertEquals(Integer.MAX_VALUE, dist[2]);  // porque arista 1->2 bloqueada
    }

    @Test
    void testDijkstraSimpleYConParada() {
        int INF = Integer.MAX_VALUE;
        int[][] grafo = {
            {0, 1, 4},
            {INF, 0, 2},
            {INF, INF, 0}
        };
        int[] distSimple = AlgoritmosModificados.dijkstraSimple(grafo, 0);
        assertEquals(0, distSimple[0]);
        assertEquals(1, distSimple[1]);
        assertEquals(3, distSimple[2]);

        // dijkstraConParada: s=0, v=1, t=2
        int res = AlgoritmosModificados.dijkstraConParada(grafo, 0, 1, 2);
        // camino 0->1 =1, 1->2=2, total=3
        assertEquals(3, res);
    }

    @Test
    void testFloydConConteo() {
        int INF = Integer.MAX_VALUE;
        int[][] grafo = {
            {0, 1, 4},
            {INF, 0, 2},
            {INF, INF, 0}
        };
        int[][] conteo = new int[3][3];
        int[][] dist = AlgoritmosModificados.floydConConteo(grafo, conteo);
        assertEquals(0, dist[0][0]);
        assertEquals(1, dist[0][1]);
        assertEquals(3, dist[0][2]);
        assertEquals(1, conteo[0][1]);
        assertEquals(1, conteo[0][2]);
    }

    @Test
    void testNodosCriticos() {
        int INF = Integer.MAX_VALUE;
        int[][] grafo = {
            {0, 1, INF},
            {1, 0, 1},
            {INF, 1, 0}
        };
        List<Integer> criticos = AlgoritmosModificados.nodosCriticos(grafo);
        // Nodo 1 es crítico porque al quitarlo se rompe la conexión entre 0 y 2
        assertTrue(criticos.contains(1));
    }

    @Test
    void testAristaMayorImpacto() {
        int INF = Integer.MAX_VALUE;
        int[][] grafo = {
            {0, 2, INF},
            {2, 0, 3},
            {INF, 3, 0}
        };
        int[] peorArista = AlgoritmosModificados.aristaMayorImpacto(grafo);
        // La arista 1->2 tiene mayor impacto si se elimina
        assertEquals(1, peorArista[0]);
        assertEquals(2, peorArista[1]);
    }

    @Test
    void testDfsModificado() {
        List<List<Integer>> grafo = new ArrayList<>();
        grafo.add(Arrays.asList(1, 2));   // nodo 0
        grafo.add(Arrays.asList(3));      // nodo 1
        grafo.add(Arrays.asList(3));      // nodo 2
        grafo.add(new ArrayList<>());     // nodo 3

        boolean[] visitado = new boolean[4];
        List<Integer> caminoActual = new ArrayList<>();
        List<List<Integer>> caminos = new ArrayList<>();
        Set<Integer> nodosProcesamiento = new HashSet<>(Arrays.asList(1, 2)); // nodos que no deben conectar

        AlgoritmosModificados.dfsModificado(0, 3, visitado, grafo, caminoActual, caminos, nodosProcesamiento);

        // Los caminos que respetan la restricción son: 0->1->3 y 0->2->3,
        // pero como 1 y 2 son ambos "procesamiento", no se debe conectar 1->2, 
        // lo que no ocurre en este grafo. Así ambos caminos deben estar presentes
        assertEquals(2, caminos.size());
        assertTrue(caminos.stream().anyMatch(c -> c.equals(Arrays.asList(0,1,3))));
        assertTrue(caminos.stream().anyMatch(c -> c.equals(Arrays.asList(0,2,3))));
    }

    @Test
    void testHeapSortDescModificado() {
        double[] arr = {5.0, 2.0, 9.0, 1.0};
        AlgoritmosModificados.heapSortDesc(arr);
        assertArrayEquals(new double[]{9.0, 5.0, 2.0, 1.0}, arr, 1e-9);
    }
}
