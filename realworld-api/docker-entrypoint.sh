#!/usr/bin/env bash

set -e

echo "127.0.0.1 $HOSTNAME" >> /etc/hosts
exec bin/realworld-api "$@"