# Makefile for generating the latex document.

PAPER_NAME=paper

all:
	-killall -q xdvi.bin
	latex -interaction=nonstopmode $(PAPER_NAME).tex
	bibtex $(PAPER_NAME)
	latex -interaction=nonstopmode $(PAPER_NAME).tex
	xdvi $(PAPER_NAME).dvi &
