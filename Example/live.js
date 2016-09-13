/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
    AppRegistry,
    StyleSheet,
    Text,
    View,
    StatusBar,
    TouchableOpacity
} from 'react-native';

import Live from 'react-native-vunun-live'

class VCLive extends Component {

    onStopPublish() {
        this.props.navigator.pop();
    }

    render() {
        return (
            <View style={styles.container}>
                <StatusBar hidden={true}/>
                <Live ref="live" style={[styles.live]}
                      streamUrl="rtmp://120.76.201.53:1935/live"
                      streamKey= "tt"
                      orientation="landspace"
                      quality="cif"
                />
                <TouchableOpacity style={{backgroundColor:'blue'}} onPress={this.onStopPublish.bind(this)}>
                    <Text>LiveView 停止直播</Text>
                </TouchableOpacity>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'red',
    },
    live:{
        position: 'absolute', top: 0, left: 0, right: 0, bottom: 0
    },

    welcome: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,
    },
    instructions: {
        textAlign: 'center',
        color: '#333333',
        marginBottom: 5,
    },
});

export default VCLive;
