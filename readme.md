# Max Probability Segment

## Overview
**Mahjong** is a java library for Chinese Breaking and written in Scala.

[![Build Status](https://secure.travis-ci.org/yingrui/mahjong.png?branch=master)](https://travis-ci.org/yingrui/mahjong)

This project implements max probability segment algorithm. The project has been well designed for Search Engine, Web Information Extraction and Nature Language Processing.

We built an independent jar file, it is easy to use, and all codes are fully tested.

It support user dictionary, synonyms, pinyin recognition etc.

## Online Demonstrate

Open link <http://mahjong.yingrui.me/>, and you could try it. 

Any question, please contact me <yingrui.f@gmail.com>.

## Experimental Development
**CRF** is the state-of-the-art algorithm for serial label classify problem, it has been widely adopted to apply to NLP tasks.

**Mahjong** also implemented CRF algorithm and trying to balance the between different algorithms.

**Deep Learning** is also become the hottest topic in machine learning. Our project also implements **Word2Vec**, it is going to integrate with max probability algorithm as well.

## Test Deep Learning Segment
1. Generate Chinese characters for character vector training: words.txt

	./scripts/generate-word2vec-training-data.sh
	
2. Train hierarchy softmax Word2Vec model for Chinese character vector

    ./scripts/run-word2vec.sh --save-file vectors.cn.test.dat --train-file words.txt -hs

3. Test Word2Vec model: the same command without parameter --train-file

    ./scripts/run-word2vec.sh --save-file vectors.cn.test.dat

4. Generate Chinese segment training data: training.txt

    ./scripts/generate-training-data.sh

5. Train Chinese segment model

    ./scripts/train-word2vec-segment.sh --train-file training.txt --save-file segment-vector.dat --word2vec-model vectors.cn.test.dat

6. Test Chinese segment model

    ./scripts/test-word2vec-segment.sh --train-file training.txt --save-file segment-vector.dat --word2vec-model vectors.cn.test.dat

## Test CRF Segment
1. Generate Chinese segment training data: training.txt

    ./scripts/generate-training-data.sh

2. Train CRF Chinese segment model

    ./scripts/train-crf-segment.sh --train-file training.txt --save-file segment-crf.m

3. Test CRF Chinese segment model

    ./scripts/test-crf-segment.sh --train-file training.txt --save-file segment-crf.m
