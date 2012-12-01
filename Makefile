# Makefile for generating the latex document.

PAPER_NAME=paper

all:
	-killall -q xdvi.bin
	latex $(PAPER_NAME).tex
	xdvi $(PAPER_NAME).dvi &
